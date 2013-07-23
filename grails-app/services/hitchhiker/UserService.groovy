package hitchhiker

import com.hitch.hiker.Constants
import com.hitch.hiker.Response
import com.hitch.hiker.User

class UserService {


    Response createUser(String username, String password, String facebookId, String deviceId) {

        def user = new User(username: username, enabled: true,
                password: password, facebookId: facebookId, deviceId: deviceId)

        if (user.save())
            return new Response(responseBody: user, status: Constants.RESPONSE_OK)
        else
            return new Response(responseBody: null, status: "Cannot save user.")


    }

    Response rateUser(String facebookId, double rating) {

        Response res = fetchUser(facebookId)

        if (res.status == Constants.RESPONSE_OK) {
            User user = (User) res.responseBody

            user.rating = ((user.numberOfRatings * user.rating) + rating) / (user.numberOfRatings + 1)
            user.numberOfRatings++
            if (user.save())
                return new Response(responseBody: user, status: Constants.RESPONSE_OK)
            else
                return new Response(responseBody: null, status: "Cannot rate user.")
        } else
            return res
    }

    Response fetchUser(String facebookId) {
        User user = User.findByFacebookId(facebookId)

        if (user)
            return new Response(responseBody: user, status: Constants.RESPONSE_OK)
        else
            return new Response(responseBody: null, status: "User not found in database.")
    }


}
