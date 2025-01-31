package com.review.study.issue.auto.repository.github.dto

class IssueCreateRequest(
    val title: String,
    val body: String,
    val labels: Set<String>,
    val assignees: Set<String>,
)