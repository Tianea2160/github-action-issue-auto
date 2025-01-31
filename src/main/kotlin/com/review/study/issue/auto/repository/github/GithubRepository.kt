package com.review.study.issue.auto.repository.github

import com.review.study.issue.auto.repository.github.dto.IssueCreateRequest
import com.review.study.issue.auto.repository.github.dto.IssueInfoResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Repository
import org.springframework.web.reactive.function.client.WebClient

@Repository
class GithubRepository(
    @Value("\${github.token}") private val token: String,
    @Value("\${github.username}") private val username: String,
    @Value("\${github.repository}") private val repoName: String
) {
    private val client: WebClient = WebClient.create("https://api.github.com")
    private val log: Logger = LoggerFactory.getLogger(GithubRepository::class.java)

    fun createIssue(
        title: String,
        body: String,
        labels: Set<String>,
        assignees: Set<String>,
    ) {
        log.info("github username: $username")
        log.info("github repository: $repoName")
        val request = IssueCreateRequest(title, body, labels, assignees)
        val response = client.post()
            .uri("/repos/${username}/${repoName}/issues")
            .headers {
                it.contentType = MediaType.parseMediaType("application/vnd.github+json")
                it.setBearerAuth(token)
                it["X-GitHub-Api-Version"] = "2022-11-28"
            }
            .bodyValue(request)
            .retrieve()
            .toBodilessEntity()
            .block()

        log.info("issue created: ${response?.statusCode}")
    }

    fun findAllIssueByLabel(label: String): List<IssueInfoResponse> {
        val response = client.get()
            .uri("/repos/${username}/${repoName}/issues?labels=${label}")
            .headers {
                it.contentType = MediaType.parseMediaType("application/vnd.github+json")
                it.setBearerAuth(token)
                it["X-GitHub-Api-Version"] = "2022-11-28"
            }
            .retrieve()
            .bodyToFlux(IssueInfoResponse::class.java)
            .collectList()
            .block()
        return response ?: emptyList()
    }

    fun closeIssue(issueNumber: Long) {
        val response = client.patch()
            .uri("/repos/${username}/${repoName}/issues/${issueNumber}")
            .headers {
                it.contentType = MediaType.parseMediaType("application/vnd.github+json")
                it.setBearerAuth(token)
                it["X-GitHub-Api-Version"] = "2022-11-28"
            }
            .bodyValue(mapOf("state" to "closed"))
            .retrieve()
            .toBodilessEntity()
            .block()
        log.info("issue(${issueNumber}) closed: ${response?.statusCode}")
    }
}

