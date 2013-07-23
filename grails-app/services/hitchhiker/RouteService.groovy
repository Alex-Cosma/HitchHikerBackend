package hitchhiker

import com.hitch.hiker.Booking
import com.hitch.hiker.Constants
import com.hitch.hiker.Response
import com.hitch.hiker.Route
import com.hitch.hiker.User

class RouteService {

    def userService

    Response fetchRoute(long routeId) {

        Route route = Route.findById(routeId)

        if (route)
            return new Response(responseBody: route, status: Constants.RESPONSE_OK)
        else
            return new Response(responseBody: null, status: "Cannot find route in database.")
    }

    Response createRoute(String hostFacebookId, float[] source, float[] destination, Date departureDate, int seatsTotal) {

        Response res = userService.fetchUser(hostFacebookId)

        if (res.status == Constants.RESPONSE_OK) {
            User host = (User) res.responseBody

            Route route = new Route(source: source, destination: destination, departureDate: departureDate, seatsTotal: seatsTotal)
            host.addToHostedRoutes(route)
            if (host.save(flush: true))
                return new Response(responseBody: host, status: Constants.RESPONSE_OK)
            else
                return new Response(responseBody: null, status: "Cannot save user in database.")
        } else
            return res
    }

    Response deleteRoute(long routeId) {

        Response res = fetchRoute(routeId)

        if (res.status == Constants.RESPONSE_OK) {

            Route route = (Route) res.responseBody
            User u = route.host
            u.removeFromHostedRoutes(route)

            return new Response(responseBody: true, status: Constants.RESPONSE_OK)

        } else
            return res
    }

    Response searchForRoute(String userFacebookId, float[] source, float[] destination, Date dateFrom, Date dateTo) {
        def query;
        def today = new Date()


        if (dateTo == null && dateFrom == null) {
            query = Route.where {
                host.facebookId != userFacebookId
            }
        } else if (dateFrom == null)
            query = Route.where {
                departureDate >= today &&
                        departureDate <= dateTo &&
                        host.facebookId != userFacebookId
            }
        else if (dateTo == null) {
            query = Route.where {
                departureDate >= dateFrom &&
                        host.facebookId != userFacebookId
            }
        } else if (dateFrom < dateTo) {
            query = Route.where {
                departureDate >= dateFrom &&
                        departureDate <= dateTo &&
                        host.facebookId != userFacebookId
            }
        } else
            return new Response(responseBody: new ArrayList<Route>(), status: "Date interval invalid.")

        ArrayList<Route> routesFound = query.findAll().toList()
        // no use in him seeing the routes he posted
        routesFound.removeAll { it.host.facebookId == userFacebookId }

        return new Response(responseBody: routesFound, status: Constants.RESPONSE_OK)


    }

    Response isHost(String userFacebookId, long routeId) {

        Response res = fetchRoute(routeId)

        if (res.status == Constants.RESPONSE_OK) {

            Route route = (Route) res.responseBody
            return new Response(responseBody: route.host.facebookId == userFacebookId, status: Constants.RESPONSE_OK)

        } else
            return res
    }

    Response getAllRoutesInWhichUserIsInvolved(String userFacebookId) {
        def query;

        Response res = userService.fetchUser(userFacebookId)
        if (res.status == Constants.RESPONSE_OK) {

            User host = (User) res.responseBody
            ArrayList<Route> routes = host.hostedRoutes.toList()
            host.bookingsMade?.toList()?.each { routes.add(it.route) }
            return new Response(responseBody: routes, status: Constants.RESPONSE_OK)

        } else
            return res
    }

    Response isParticipating(String userFacebookId, long routeId) {
        Response res = userService.fetchUser(userFacebookId)
        if (res.status == Constants.RESPONSE_OK) {
            User user = (User) res.responseBody

            res = fetchRoute(routeId)

            if (res.status == Constants.RESPONSE_OK) {
                Route route = (Route) res.responseBody

                def bookingsMade = user.bookingsMade?.toList()

                if (bookingsMade != null)
                    for (Booking b : bookingsMade) {
                        if (b.route.id == routeId)
                            return new Response(responseBody: route, status: Constants.RESPONSE_OK)
                    }
                else
                    return new Response(responseBody: null, status: "User not participating in route.")

                // got here, then not participating
                return new Response(responseBody: null, status: "User not participating in route.")
            } else
                return res
        } else
            return res
    }
}
