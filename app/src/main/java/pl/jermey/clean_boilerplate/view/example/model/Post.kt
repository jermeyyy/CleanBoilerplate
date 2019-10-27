package pl.jermey.clean_boilerplate.view.example.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Post(
  val userId: Long,
  val id: Long,
  val title: String,
  val body: String
) : Parcelable