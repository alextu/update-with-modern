package com.gradle.enterprise.update

import groovy.transform.ToString
import groovy.transform.TupleConstructor

@TupleConstructor
@ToString
class Repository {
    String origin
    String path
    String branch
    String status
}
