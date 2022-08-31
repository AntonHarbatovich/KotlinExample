package ru.skillbranch.kotlinexample

object UserHolder {
    private val map = mutableMapOf<String, User>()

    fun resisterUser(
        fullName: String,
        email: String,
        password: String
    ): User {
        return User.makeUser(fullName, email = email, password = password)
            .also { user ->
                if (map.containsKey(user.login)) {
                    throw IllegalArgumentException("A user with this email already exists")
                } else {
                    map[user.login] = user
                }
            }
    }

    fun loginUser(login: String, password: String): String? {
        return map[login.trim()]?.run {
            if (checkPassword(password)) this.userInfo
            else null
        }
    }
}