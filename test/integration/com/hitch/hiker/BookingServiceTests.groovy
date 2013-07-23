package com.hitch.hiker

import static org.junit.Assert.*
import org.junit.*

class BookingServiceTests {

    def routeService
    def userService
    def bookingService

    @Before
    void setUp() {
        // Setup logic here
    }

    @After
    void tearDown() {
        // Tear down logic here
    }

    @Test
    void testCreation() {

        Response res = userService.createUser("user1", "pass", "1234", "1234")
        Assert.assertTrue(res.status == Constants.RESPONSE_OK)
        User u1 = (User) res.responseBody

        res = userService.createUser("user2", "pass", "12", "1234")
        Assert.assertTrue(res.status == Constants.RESPONSE_OK)
        User u2 = (User) res.responseBody

        float[] source = [99.9, 99.9]
        float[] destination = [88.8, 88.8]
        Date departureDate = new Date()
        int seatsTotal = 5

        res = routeService.createRoute(u1.facebookId, source, destination, departureDate, seatsTotal)
        Assert.assertTrue(res.status == Constants.RESPONSE_OK)
        u1 = (User) res.responseBody

        res = routeService.createRoute(u1.facebookId, source, destination, departureDate, seatsTotal)
        Assert.assertTrue(res.status == Constants.RESPONSE_OK)
        u1 = (User) res.responseBody

        res = routeService.createRoute(u1.facebookId, source, destination, departureDate, seatsTotal)
        Assert.assertTrue(res.status == Constants.RESPONSE_OK)
        u1 = (User) res.responseBody


        res = routeService.createRoute(u2.facebookId, source, destination, departureDate, seatsTotal)
        Assert.assertTrue(res.status == Constants.RESPONSE_OK)
        u2 = (User) res.responseBody

        ArrayList<Route> routes1 = u1.getHostedRoutes().toList() // 3
        ArrayList<Route> routes2 = u2.getHostedRoutes().toList() // 1

        int initSeatsTaken = routes2.get(0).seatsTaken

        res = bookingService.createBooking(u1.facebookId, routes2.get(0).id, 1)
        Assert.assertTrue(res.status == Constants.RESPONSE_OK)
        Route r1 = ((Booking) res.responseBody).route
        Assert.assertTrue(initSeatsTaken == (r1.seatsTaken - 1))

        // testing that a user cannot book his own hosted route
        res = bookingService.createBooking(u2.facebookId, routes2.get(0).id, 1)
        Assert.assertFalse(res.status == Constants.RESPONSE_OK)

        res = bookingService.createBooking(u2.facebookId, routes1.get(0).id, 1)
        Assert.assertTrue(res.status == Constants.RESPONSE_OK)
        Route r2 = ((Booking) res.responseBody).route

        Assert.assertTrue(r1.getBookings().toList().get(0).id != null)
        Assert.assertTrue(r1.getBookings().size() == 1)
        Assert.assertTrue(r2.getBookings().size() == 1)
    }

    @Test
    void testFetchAndDelete(){
        Response res = userService.createUser("user1", "pass", "1234", "1234")
        Assert.assertTrue(res.status == Constants.RESPONSE_OK)
        User u1 = (User) res.responseBody

        res = userService.createUser("user2", "pass", "12", "1234")
        Assert.assertTrue(res.status == Constants.RESPONSE_OK)
        User u2 = (User) res.responseBody

        float[] source = [99.9, 99.9]
        float[] destination = [88.8, 88.8]
        Date departureDate = new Date()
        int seatsTotal = 5

        res = routeService.createRoute(u1.facebookId, source, destination, departureDate, seatsTotal)
        Assert.assertTrue(res.status == Constants.RESPONSE_OK)
        u1 = (User) res.responseBody

        res = routeService.createRoute(u1.facebookId, source, destination, departureDate, seatsTotal)
        Assert.assertTrue(res.status == Constants.RESPONSE_OK)
        u1 = (User) res.responseBody

        res = routeService.createRoute(u1.facebookId, source, destination, departureDate, seatsTotal)
        Assert.assertTrue(res.status == Constants.RESPONSE_OK)
        u1 = (User) res.responseBody


        res = routeService.createRoute(u2.facebookId, source, destination, departureDate, seatsTotal)
        Assert.assertTrue(res.status == Constants.RESPONSE_OK)
        u2 = (User) res.responseBody

        ArrayList<Route> routes1 = u1.getHostedRoutes().toList() // 3
        ArrayList<Route> routes2 = u2.getHostedRoutes().toList() // 1

        int initSeatsTaken = routes2.get(0).seatsTaken

        res = bookingService.createBooking(u1.facebookId, routes2.get(0).id, 1)
        Assert.assertTrue(res.status == Constants.RESPONSE_OK)
        Booking booking = (Booking) res.responseBody

        res = bookingService.fetchBooking(booking.id)
        Assert.assertTrue(res.status == Constants.RESPONSE_OK)

        res = bookingService.deleteBooking(u1.facebookId,booking.id)
        Assert.assertTrue(res.status == Constants.RESPONSE_OK)

        res = bookingService.deleteBooking(u1.facebookId,booking.id)
        Assert.assertFalse(res.status == Constants.RESPONSE_OK)
    }
}
