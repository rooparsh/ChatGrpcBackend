package me.anmolverma.domain

import me.anmolverma.services.users.SocialType
import me.anmolverma.services.users.User
import org.bson.types.ObjectId

data class AppUser(
    val _id: ObjectId? = null,
    val userId: Int,
    val uname: String,
    val displayName: String,
    val email: String,
    val phone: String,
    val socialType: SocialType
)


fun User.toAppUser(): AppUser {
    return AppUser(
        userId = this.id,
        uname = this.uname,
        displayName = this.displayName,
        email = this.email,
        phone = this.phone,
        socialType = this.socialType
    )
}