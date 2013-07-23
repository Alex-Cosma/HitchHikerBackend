package com.hitch.hiker

class Route {
    float[] source
    float[] destination

    Date departureDate

    String hostPhone
    String hostAdditionalNote


    int seatsTotal
    int seatsTaken
    String price 

    static belongsTo = [host : User]
    static hasMany = [bookings : Booking]


    static constraints = {
        bookings lazy:false, cascade:"all,delete-orphan"
        source nullable: false
        destination nullable: false
        departureDate nullable: false
        seatsTotal nullable: false
        hostPhone nullable: true
        hostAdditionalNote nullable: true
        seatsTaken nullable: true
        price nullable: true

    }
}
