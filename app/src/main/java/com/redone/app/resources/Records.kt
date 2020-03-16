package com.redone.app.resources

import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "ValCurs", strict = false)
data class Records constructor(
    @field:ElementList(name = "Record", inline = true)
    var quotesList: List<Quote>? = null
)



