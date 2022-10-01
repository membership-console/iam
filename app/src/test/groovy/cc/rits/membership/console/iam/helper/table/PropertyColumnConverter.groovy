package cc.rits.membership.console.iam.helper.table

class PropertyColumnConverter {
    Column getProperty(final String property) {
        new Column(name: property)
    }
}
