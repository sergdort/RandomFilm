package com.randofilm.sergdort.platform.Conversion

interface DomainConvertible<D> {
    fun asDomain(): D
}
