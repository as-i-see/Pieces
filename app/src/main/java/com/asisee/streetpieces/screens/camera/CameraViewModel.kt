package com.asisee.streetpieces.screens.camera

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.lifecycle.viewModelScope
import com.asisee.streetpieces.R
import com.asisee.streetpieces.model.datasource.FileDataSource
import com.asisee.streetpieces.model.service.LogService
import com.asisee.streetpieces.screens.LogViewModel
import com.ramcosta.composedestinations.spec.Direction
import com.ujizin.camposer.state.CameraState
import com.ujizin.camposer.state.ImageCaptureResult
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class CameraViewModel
@Inject
constructor(
    logService: LogService,
    private val fileDataSource: FileDataSource,
     @ApplicationContext private val context: Context,
) : ContainerHost<CameraScreenState, CameraSideEffect>, LogViewModel(logService) {

    override val container: Container<CameraScreenState, CameraSideEffect> = container(CameraScreenState)

    fun takePicture(openScreen: (Direction) -> Unit, cameraState: CameraState) =
        with(cameraState) {
            viewModelScope.launch {
                when {
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ->
                        takePicture(
                            fileDataSource.imageContentValues,
                        ) {
                            onImageResult(it)
                        }
                    else ->
                        takePicture(fileDataSource.getFile("jpg")) { onImageResult(it) }
                }
            }
        }

    private fun onImageResult(imageResult: ImageCaptureResult) = intent {
        postSideEffect(when (imageResult) {
            is ImageCaptureResult.Error -> CameraSideEffect.Error(imageResult.throwable.message ?:
                context.getString(R.string.unknown_error))
            is ImageCaptureResult.Success ->
                    CameraSideEffect.NavigateWithPhotoResult(imageResult.savedUri?:Uri.EMPTY)
        })
    }

    fun onGalleryClick() = Unit // TODO
}
