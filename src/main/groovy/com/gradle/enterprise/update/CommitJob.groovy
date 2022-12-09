package com.gradle.enterprise.update

import groovy.transform.ToString
import groovy.transform.TupleConstructor

@TupleConstructor
@ToString
class CommitJob {

    String state
    String resultLink

}
