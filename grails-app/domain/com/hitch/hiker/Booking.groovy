package com.hitch.hiker

class Booking {

    int seatsTaken
    float userRatingOfHost

    static belongsTo = [user: User, route: Route]

    static constraints = {
        seatsTaken nullable: false
        userRatingOfHost nullable: true
    }
}
