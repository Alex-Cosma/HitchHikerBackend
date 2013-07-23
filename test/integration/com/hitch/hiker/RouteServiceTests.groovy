package com.hitch.hiker

import static org.junit.Assert.*
import org.junit.*

class RouteServiceTests {

    def userService
    def routeService
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
        User user = (User) res.responseBody

        float[] source = [99.9, 99.9]
        float[] destination = [88.8, 88.8]
        Date departureDate = new Date()
        int seatsTotal = 5

        res = routeService.createRoute(user.facebookId, source, destination, departureDate, seatsTotal)
        Assert.assertTrue(res.status == Constants.RESPONSE_OK)
        user = (User) res.responseBody

        Assert.assertNotNull(user)
        Assert.assertEquals(user.getHostedRoutes().size(), 1)

    }

    @Test
    void testFetchAndDelete() {
        Response res = userService.createUser("user1", "pass", "1234", "1234")
        Assert.assertTrue(res.status == Constants.RESPONSE_OK)
        User user = (User) res.responseBody

        float[] source = [99.9, 99.9]
        float[] destination = [88.8, 88.8]
        Date departureDate = new Date()
        int seatsTotal = 5

        res = routeService.createRoute(user.facebookId, source, destination, departureDate, seatsTotal)
        Assert.assertTrue(res.status == Constants.RESPONSE_OK)
        user = (User) res.responseBody

        Assert.assertNotNull(user)
        Assert.assertEquals(user.getHostedRoutes().size(), 1)

        res = routeService.fetchRoute(user.getHostedRoutes().asList().get(0).id)
        Assert.assertTrue(res.status == Constants.RESPONSE_OK)

        Route r = (Route) res.responseBody

        Assert.assertEquals(user.getHostedRoutes().asList().get(0), r)

        res = routeService.deleteRoute(r.id)
        Assert.assertTrue(res.status == Constants.RESPONSE_OK && (res.responseBody == true))

        res = routeService.deleteRoute(r.id)
        Assert.assertTrue(res.status != Constants.RESPONSE_OK)

    }

    @Test
    void testRouteSearch() {

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

        res = routeService.searchForRoute(u1.facebookId, source, destination, new Date() - 1, null)
        Assert.assertTrue(res.status == Constants.RESPONSE_OK)
        ArrayList<Route> r1 = (ArrayList<Route>) res.responseBody

        res = routeService.searchForRoute(u2.facebookId, source, destination, new Date() - 1, null)
        Assert.assertTrue(res.status == Constants.RESPONSE_OK)
        ArrayList<Route> r2 = (ArrayList<Route>) res.responseBody

        res = routeService.searchForRoute(u2.facebookId, source, destination, new Date() + 1, null)
        Assert.assertTrue(res.status == Constants.RESPONSE_OK)
        ArrayList<Route> r3 = (ArrayList<Route>) res.responseBody

        res = routeService.searchForRoute(u2.facebookId, source, destination, new Date() - 1, new Date() + 1)
        Assert.assertTrue(res.status == Constants.RESPONSE_OK)
        ArrayList<Route> r4 = (ArrayList<Route>) res.responseBody

        res = routeService.searchForRoute(u1.facebookId, source, destination, new Date() - 1, new Date() + 1)
        Assert.assertTrue(res.status == Constants.RESPONSE_OK)
        ArrayList<Route> r5 = (ArrayList<Route>) res.responseBody

        res = routeService.searchForRoute(u1.facebookId, source, destination, new Date() + 1, new Date() + 2)
        Assert.assertTrue(res.status == Constants.RESPONSE_OK)
        ArrayList<Route> r6 = (ArrayList<Route>) res.responseBody

        res = routeService.searchForRoute(u1.facebookId, source, destination, null, new Date() + 2)
        Assert.assertTrue(res.status == Constants.RESPONSE_OK)
        ArrayList<Route> r7 = (ArrayList<Route>) res.responseBody

        res = routeService.searchForRoute(u1.facebookId, source, destination, null, new Date() + 2)
        Assert.assertTrue(res.status == Constants.RESPONSE_OK)
        ArrayList<Route> r8 = (ArrayList<Route>) res.responseBody


        Assert.assertTrue(r1.size() == 1)
        Assert.assertTrue(r2.size() == 3)
        Assert.assertTrue(r3.size() == 0)
        Assert.assertTrue(r4.size() == 3)
        Assert.assertTrue(r5.size() == 1)
        Assert.assertTrue(r6.size() == 0)
        Assert.assertTrue(r7.size() == 0)
        Assert.assertTrue(r8.size() == 0)

    }

    @Test
    void testIsHost() {
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

        res = routeService.createRoute(u2.facebookId, source, destination, departureDate, seatsTotal)
        Assert.assertTrue(res.status == Constants.RESPONSE_OK)
        u2 = (User) res.responseBody

        Boolean aBoolean = (Boolean) routeService.isHost(u1.facebookId, u1.getHostedRoutes().toList().get(0).id).responseBody
        Assert.assertTrue(aBoolean)

        aBoolean = (Boolean)routeService.isHost(u2.facebookId, u2.getHostedRoutes().toList().get(0).id).responseBody
        Assert.assertTrue(aBoolean)

        aBoolean =(Boolean) routeService.isHost(u1.facebookId, u2.getHostedRoutes().toList().get(0).id).responseBody
        Assert.assertFalse(aBoolean)

        aBoolean =(Boolean) routeService.isHost(u2.facebookId, u1.getHostedRoutes().toList().get(0).id).responseBody
        Assert.assertFalse(aBoolean)

    }

    @Test
    void testMyRoutes() {
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


        res = routeService.getAllRoutesInWhichUserIsInvolved(u1.facebookId)
        Assert.assertTrue(res.status == Constants.RESPONSE_OK)
        ArrayList<Route> totalRoutes1 = (ArrayList<Route>) res.responseBody
        Assert.assertTrue(totalRoutes1.size() == 4)

        res = routeService.getAllRoutesInWhichUserIsInvolved(u2.facebookId)
        Assert.assertTrue(res.status == Constants.RESPONSE_OK)
        ArrayList<Route> totalRoutes2 = (ArrayList<Route>) res.responseBody
        Assert.assertTrue(totalRoutes2.size() == 2)
    }

    @Test
    void testUserParticipation(){
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

        res = bookingService.createBooking(u1.facebookId,u2.hostedRoutes.toList().get(0).id,1)
        Assert.assertTrue(res.status == Constants.RESPONSE_OK)
        Route bookedRoute = ((Booking) res.responseBody).route

        res = routeService.isParticipating(u1.facebookId,bookedRoute.id)
        Assert.assertTrue(res.status == Constants.RESPONSE_OK)

        res = routeService.isParticipating(u2.facebookId,bookedRoute.id)
        Assert.assertFalse(res.status == Constants.RESPONSE_OK)

        res = routeService.isParticipating(u2.facebookId,u1.hostedRoutes.toList().get(1).id)
        Assert.assertFalse(res.status == Constants.RESPONSE_OK)

    }
}
