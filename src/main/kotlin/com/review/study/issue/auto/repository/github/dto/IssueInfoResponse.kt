package com.review.study.issue.auto.repository.github.dto

class IssueInfoResponse(
    val id: Long,
    val title: String,
    val state: String,
    val number: Long,
)