package com.hitch.hiker

class User {

    transient springSecurityService

    String username
    String password
    boolean enabled
    boolean accountExpired
    boolean accountLocked
    boolean passwordExpired

    static hasMany = [hostedRoutes: Route, bookingsMade: Booking]

    String facebookId
    String deviceId
    int numberOfRatings
    float rating

    static constraints = {
        hostedRoutes lazy: false
        bookingsMade lazy: false
        username blank: false, unique: true
        password blank: false
    }

    static mapping = {
        hostedRoutes lazy:false, cascade:"all,delete-orphan"
        bookingsMade lazy:false, cascade:"all,delete-orphan"
        password column: '`password`'
    }

    Set<Role> getAuthorities() {
        UserRole.findAllByUser(this).collect { it.role } as Set
    }

    def beforeInsert() {
        encodePassword()
    }

    def beforeUpdate() {
        if (isDirty('password')) {
            encodePassword()
        }
    }

    protected void encodePassword() {
        password = springSecurityService.encodePassword(password)
    }
}
