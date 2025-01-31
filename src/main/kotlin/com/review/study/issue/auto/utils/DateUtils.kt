package com.review.study.issue.auto.utils

import java.time.ZonedDateTime
import java.time.temporal.WeekFields


fun ZonedDateTime.weekOfYear(): Int = this.toLocalDate()[WeekFields.ISO.weekOfYear()]
