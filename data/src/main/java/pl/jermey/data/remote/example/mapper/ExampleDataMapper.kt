package pl.jermey.data.remote.example.mapper

import pl.jermey.data.mapper.EntityMapper
import pl.jermey.data.remote.example.response.ExampleResponse
import pl.jermey.domain.model.example.PostData

class ExampleDataMapper : EntityMapper<ExampleResponse, List<PostData>> {
    override fun mapFromRemote(type: ExampleResponse): List<PostData> {
        return type
    }
}