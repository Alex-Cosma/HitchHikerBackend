package hitchhiker

import com.hitch.hiker.Booking
import com.hitch.hiker.Constants
import com.hitch.hiker.Response
import com.hitch.hiker.Route
import com.hitch.hiker.User

class BookingService {

    def userService
    def routeService

    Response fetchBooking(long bookingId) {
        Booking booking = Booking.findById(bookingId)

        if (booking)
            return new Response(responseBody: booking, status: Constants.RESPONSE_OK)
        else
            return new Response(responseBody: null, status: "Booking not found in database.")

    }

    Response createBooking(String userFacebookId, long routeId, int seatsTaken) {

        Response res = userService.fetchUser(userFacebookId)
        if (res.status == Constants.RESPONSE_OK) {
            User joiningUser = (User) res.responseBody

            res = routeService.fetchRoute(routeId)
            if (res.status == Constants.RESPONSE_OK) {

                Route route = (Route) res.responseBody

                // check if someone is trying to book his own route
                if (route.host.facebookId != userFacebookId) {
                    // first check to see if the route has enough available seats
                    if ((route.seatsTaken + seatsTaken) <= route.seatsTotal) {

                        Booking booking = new Booking(seatsTaken: seatsTaken)
                        booking.user = joiningUser
                        booking.route = route
                        booking.save()
                        route.seatsTaken += seatsTaken
                        joiningUser.addToBookingsMade(booking)
                        route.addToBookings(booking)

                        // if both things can be saved
                        if (joiningUser.save()) {
                            if (route.save()) {
                                return new Response(responseBody: booking, status: Constants.RESPONSE_OK)
                            } else
                                return new Response(responseBody: null, status: "Route cannot be saved with the new booking.")
                        } else
                            return new Response(responseBody: null, status: "User cannot be saved with the new booking.")


                    } else
                        return new Response(responseBody: null, status: "Cannot create booking. Not enough available seats.")
                } else
                    return new Response(responseBody: null, status: "A user cannot make a booking on a route which he / she hosted.")
            } else
                return res
        } else
            return res
    }

    Response deleteBooking(String userFacebookId, long bookingId) {

        Response res = userService.fetchUser(userFacebookId)
        if (res.status == Constants.RESPONSE_OK) {
            User user = (User) res.responseBody

            res = fetchBooking(bookingId)
            if (res.status == Constants.RESPONSE_OK) {
                Booking booking = (Booking) res.responseBody

                if (user.bookingsMade.contains(booking)) {
                    user.removeFromBookingsMade(booking)
                    booking.route.removeFromBookings(booking)
                    return new Response(responseBody: true, status: Constants.RESPONSE_OK)
                } else
                    return new Response(responseBody: null, status: "Booking not belonging to specified user.")
            } else return res
        } else
            return res
    }


}
