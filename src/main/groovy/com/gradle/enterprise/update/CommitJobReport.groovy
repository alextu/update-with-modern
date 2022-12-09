package com.gradle.enterprise.update

import groovy.transform.ToString
import groovy.transform.TupleConstructor

@TupleConstructor
@ToString
class CommitJobReport {

    int completed
    SummaryResult summaryResult
    List<CommitJob> commits

}
