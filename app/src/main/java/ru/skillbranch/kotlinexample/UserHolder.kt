package ru.skillbranch.kotlinexample

import androidx.annotation.VisibleForTesting

object UserHolder {
    private val map = mutableMapOf<String, User>()

    fun registerUser(
        fullName: String,
        email: String,
        password: String
    ): User {
        val user = User.makeUser(fullName, email = email, password = password)
        return when (map.contains(user.login)) {
            true -> throw IllegalArgumentException("A user with this email already exists")
            else -> user.also { map[user.login] = user }
        }
    }

    fun registerUserByPhone(fullName: String, rawPhone: String): User {
        val user = User.makeUser(fullName, phone = rawPhone)
        val regex = Regex("^(\\+)(\\d{11})$")
        return if (regex.matches(user.login)) {
            when (map.contains(user.login)) {
                true -> throw IllegalArgumentException("A user with this phone already exists")
                else -> user.also { map[it.login] = it }
            }
            user.also {
                map[it.login] = it
            }
        } else {
            throw IllegalArgumentException("Enter a valid phone number starting with a + and containing 11 digits")
        }
    }

    fun requestAccessCode(login: String) {
        val _login = login.replace("[^+\\d]".toRegex(), "")
        map[_login]?.run {
            changeAccessCode(_login)
        }
    }

    fun loginUser(login: String, password: String): String? {
        val checkLogin: String = if (login.contains("+")) login.replace("[^+\\d]".toRegex(), "")
        else login
        println(map[checkLogin.trim()])
        return map[checkLogin.trim()]?.run {
            if (checkPassword(password)) this.userInfo
            else null
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun clearHolder() {
        map.clear()
    }
}