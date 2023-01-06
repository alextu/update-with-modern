package com.gradle.enterprise.update

import spock.lang.Specification

class CommitJobReportTest extends Specification {
    def "should display report as expected"() {
        given:
        CommitJobReport report = new CommitJobReport(2, new SummaryResult(4, 1, 1, 1),
            [
                new CommitJob('FINISHED', 'https://github.com/alextu/gradle-sample-2/pull/1'),
                new CommitJob('NO_CHANGES', 'https://github.com/alextu/gradle-sample-1/pull/1'),
                new CommitJob('FAILED', null),
                new CommitJob('PROCESSING', null)
            ])
        println report.toDisplayable()

        expect:
        report.toDisplayable() ==
            '''------------------------------Pull request status-------------------------------
Job completed                 Yes
--------------------------------------------------------------------------------
FINISHED                      https://github.com/alextu/gradle-sample-2/pull/1
NO_CHANGES                    https://github.com/alextu/gradle-sample-1/pull/1
FAILED                        N/A
PROCESSING                    N/A
'''
    }
}
