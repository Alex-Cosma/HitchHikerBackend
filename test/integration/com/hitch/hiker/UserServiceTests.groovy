package com.hitch.hiker

import static org.junit.Assert.*
import org.junit.*

class UserServiceTests {
    def userService
    transient springSecurityService

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

        int initialCount = User.list().size()

        String username = "user1"
        String password = "password1"
        String deviceId = "1234"
        String facebookId = "4321"

        Response res = userService.createUser(username, password, facebookId, deviceId)

        Assert.assertTrue(res.status == Constants.RESPONSE_OK)

        User user = (User) res.responseBody
        Assert.assertNotNull(user)
        Assert.assertEquals(user.getUsername(), username)
        Assert.assertEquals(user.getPassword(), springSecurityService.encodePassword(password))
        Assert.assertEquals(user.getFacebookId(), facebookId)
        Assert.assertEquals(user.getDeviceId(), deviceId)


        int finalCount = User.list().size()

        Assert.assertFalse(initialCount == finalCount)
        Assert.assertTrue(initialCount == (finalCount - 1))

    }

    @Test
    void testRate() {
        String username = "user1"
        String password = "password1"
        String deviceId = "1234"
        String facebookId = "4321"

        Response res = userService.createUser(username, password, facebookId, deviceId)

        Assert.assertTrue(res.status == Constants.RESPONSE_OK)

        User user = (User) res.responseBody

        res = userService.rateUser(facebookId, 5.0)
        Assert.assertTrue(res.status == Constants.RESPONSE_OK)
        user = (User) res.responseBody
        Assert.assertNotNull(user)
        Assert.assertEquals(user.rating, 5.0, 0.1)
        res = userService.rateUser(facebookId, 9.0)
        Assert.assertTrue(res.status == Constants.RESPONSE_OK)
        user = (User) res.responseBody
        Assert.assertNotNull(user)
        Assert.assertEquals(user.rating, 7.0, 0.1)

    }
}
