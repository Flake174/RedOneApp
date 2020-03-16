package com.redone.app.resources

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "Record", strict = false)
data class Quote constructor (

    @field:Attribute(name = "Date") var date: String? = null,

    @field:Element(name = "Nominal") var nominal: String? = null,

    @field:Element(name = "Value") var value: String? = null

)