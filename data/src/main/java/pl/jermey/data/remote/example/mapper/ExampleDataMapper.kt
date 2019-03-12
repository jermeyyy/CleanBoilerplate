package pl.jermey.data.remote.example.mapper

import pl.jermey.data.mapper.EntityMapper
import pl.jermey.data.remote.example.response.ExampleResponse
import pl.jermey.domain.model.example.ExampleModel

class ExampleDataMapper : EntityMapper<ExampleResponse, List<ExampleModel>> {
    override fun mapFromRemote(type: ExampleResponse): List<ExampleModel> {
        return type.data
    }
}