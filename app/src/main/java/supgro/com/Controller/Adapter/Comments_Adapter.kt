package supgro.com.Controller.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView
import supgro.com.Controller.Model.Comment
import supgro.com.Controller.Model.User
import supgro.com.R

class Comments_Adapter(val mContext: Context, val mComment: MutableList<Comment>?
): RecyclerView.Adapter<Comments_Adapter.Holder>() {

    private var firebaseUser: FirebaseUser? = null



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.comment_layout, parent, false)
        return Holder(view)

    }



    override fun onBindViewHolder(holder: Holder, position: Int) {

        firebaseUser = FirebaseAuth.getInstance().currentUser

        val comment = mComment!![position]
//user_comments is where the comment shows up.
        holder.usersComments.text = comment.getComment()
        //holder.username.text = comment.getPublisher()
//create a function
        getUserInfo(holder.commentProfileImage, holder.username, comment.getPublisher())
        
        

    }

    private fun getUserInfo(commentProfileImage: CircleImageView, username: TextView, publisher: String) {

        val UserRef = FirebaseDatabase.getInstance()
            .reference.child("username")
            .child(publisher)

        UserRef.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()){
                    val user = p0.getValue(User::class.java)
                    Glide.with(mContext).load(user!!.getImage()).placeholder(R.drawable.avataphoto).into(commentProfileImage)

                    username.text = user!!.getUsername()
                }
            }

            override fun onCancelled(p0: DatabaseError) {


            }
        })

    }


    override fun getItemCount(): Int {
        return mComment!!.size


    }


    inner class Holder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView){

        var commentProfileImage: CircleImageView = itemView.findViewById(R.id.username_image_comment)
        var username: TextView = itemView.findViewById(R.id.username_comment)
        var usersComments: TextView = itemView.findViewById(R.id.user_comments)


    }
}