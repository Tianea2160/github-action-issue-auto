package com.review.study.issue.auto

import com.review.study.issue.auto.application.TemplateService
import com.review.study.issue.auto.repository.github.GithubRepository
import com.review.study.issue.auto.repository.user.UserRepository
import jakarta.annotation.PostConstruct
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.time.ZoneId
import java.time.ZonedDateTime


@SpringBootApplication
class IssueAutoApplication(
    private val githubRepository: GithubRepository,
    private val userRepository: UserRepository,
    private val templateService: TemplateService,
) {
    private val log: Logger = LoggerFactory.getLogger(IssueAutoApplication::class.java)
    private val WEEKLY_REVIEW_ISSUE_BODY_TEMPLATE: String = ("# 이번주 목표\n"
            + "- [ ] 목표 1. \n"
            + "\n"
            + "\n"
            + "# 회고\n"
            + "## 1. 목표 1에 대한 회고\n"
            + "\n"
            + "\n"
            + "# 다음주 목표\n"
            + "1.\n")

    @PostConstruct
    fun init() {
        // 현재 주 label
        val now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
        val currentWeekLabel = templateService.createLabel(now)

        // 이전 주 label
        val previousWeekNow = now.minusWeeks(1L)
        val previousWeekLabel = templateService.createLabel(previousWeekNow)

        log.info(currentWeekLabel)
        log.info(previousWeekLabel)

        // 이전 주 라벨로 이슈 조회
        val issues = githubRepository.findAllIssueByLabel(currentWeekLabel)
        // 이전 주 이슈 close
        issues.forEach { githubRepository.closeIssue(it.number) }

        // 현재 이슈 create
        val users = userRepository.findAll()

        users.forEach {
            val title = templateService.createIssueTitle(it.username)
            githubRepository.createIssue(
                title = title,
                body = WEEKLY_REVIEW_ISSUE_BODY_TEMPLATE,
                labels = setOf(currentWeekLabel),
                assignees = setOf(it.loginId),
            )
        }
    }
}


fun main(args: Array<String>) {
    runApplication<IssueAutoApplication>(*args)
}

