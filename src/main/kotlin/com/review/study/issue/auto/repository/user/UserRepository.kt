package com.review.study.issue.auto.repository.user

import org.springframework.stereotype.Component

@Component
class UserRepository {
    fun findAll(): List<User> {
        return listOf(
            User(username = "조현준", loginId = "Tianea2160"),
            User(username = "김찬웅", loginId = "Dove-kim"),
            User(username = "김준우", loginId = "Junuu"),
            User(username = "김도엽", loginId = "BrianDYKim"),
            User(username = "우경준", loginId = "Jay-WKJun"),
            User(username = "곽다희", loginId = "daadaadaah"),
        )
    }
}

class User(
    val loginId: String,
    val username: String,
)