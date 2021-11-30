package domain.usecase.photo

import data.remote.DownloadDataState
import domain.repository.PhotoRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@ExperimentalCoroutinesApi
class DownloadUseCase @Inject constructor(
    private val repository: PhotoRepository,
) {
    operator fun invoke(url: String, path: String, name: String): Flow<DownloadDataState> = channelFlow {
        send(DownloadDataState.Loading)
        repository.download(url, path, name).collectLatest {
            send(it)
        }
    }
}