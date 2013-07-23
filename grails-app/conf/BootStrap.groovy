import com.hitch.hiker.Role
import com.hitch.hiker.User

class BootStrap {

    def init = { servletContext ->

        def adminRole = Role.findOrSaveByAuthority('ROLE_ADMIN')

        def userRole = Role.findOrSaveByAuthority('ROLE_USER')

        def admin = new User(username: "admin", enabled: true,
                password: 'admin')
        admin.save()
    }
    def destroy = {
    }
}
