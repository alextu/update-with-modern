package com.gradle.enterprise.update

import spock.lang.Specification

class RepositoriesReportTest extends Specification {
    def "should display report as expected"() {
        given:
        RepositoriesReport report = new RepositoriesReport([
            new Repository('github.com', 'alextu/gradle-sample-1', 'main', 'FINISHED'),
            new Repository('github.com', 'alextu/gradle-sample-2', 'main', 'FINISHED'),
            new Repository('github.com', 'alextu/foo', 'main', 'FAILED')
        ])
        println report.toDisplayable()

        expect:
        report.toDisplayable() ==
            '''---------------------------Recipe run on repos status---------------------------
alextu/gradle-sample-1          FINISHED
alextu/gradle-sample-2          FINISHED
alextu/foo                        FAILED
'''
    }
}
