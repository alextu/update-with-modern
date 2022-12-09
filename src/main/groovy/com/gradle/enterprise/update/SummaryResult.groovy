package com.gradle.enterprise.update

import groovy.transform.ToString
import groovy.transform.TupleConstructor

@TupleConstructor
@ToString
class SummaryResult {

    int count
    int successfulCount
    int failedCount
    int noChangeCount

}
