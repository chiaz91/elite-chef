package ntu.platform.cookery.data.firebase

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.firebase.ui.auth.data.model.User
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import ntu.platform.cookery.data.entity.*

// firebase realtime database

private const val TAG = "Cy.FB.Database"
private const val LOADING_IMAGE_URL = "https://www.google.com/images/spin-32.gif"
object FBDatabaseRepository{
    private lateinit var db: FirebaseDatabase


    init{
        if (BuildConfig.DEBUG) {
            Log.i(TAG, "Fb.database init")
//            Firebase.database.useEmulator("10.0.2.2", 9000)
        }
        //FirebaseDatabase.getInstance("url if needed")
        db = Firebase.database

        // TODO, test offline
        db.setPersistenceEnabled(true)
    }

    fun saveUser(user: ECUser) {
        db.reference.child(REF_USERS).child(user.uid!!).setValue(user)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    Log.d(TAG, "saveUser success")
                } else {
                    Log.d(TAG, "saveUser failed with ${it.exception!!.message}")
                }
            }
    }

    fun getUser(uid: String = FBAuthRepository.getUser()!!.uid): MutableLiveData<ECUser>{
        val result = MutableLiveData<ECUser>()
//        db.reference.child(REF_USERS).child(uid).get()
//            .addOnSuccessListener {
//                val user = it.getValue<ECUser>()!!
//                user.uid = uid
//                result.value = user
//            }
//            .addOnFailureListener{
//                Log.e(TAG, "getUser failed, err=${it.message}")
//            }
        db.reference.child(REF_USERS).child(uid).addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue<ECUser>()
                user?.let {
                    it.uid = uid
                    result.value = it
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "getRecipe failed, err=${error.message}")
            }

        })
        return result
    }

    fun hasFollowUser(followingUserId: String, userId: String? = FBAuthRepository.getUser()!!.uid): MutableLiveData<Boolean>{
        val result = MutableLiveData<Boolean>(false)
        db.reference.child(REF_USERS_FOLLOW).child(userId!!).child(followingUserId)
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue<ECUser>()
                    result.value = (user!=null)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "hasFollowUser failed, err=${error.message}")
                }
            })
        return result
    }

    // User Follow
    fun followUser(followingUser: ECUser, userId: String? = FBAuthRepository.getUser()!!.uid){
        db.reference.child(REF_USERS_FOLLOW).child(userId!!).child(followingUser.uid!!).setValue(followingUser)
            .addOnSuccessListener {
                Log.e(TAG, "followUser Success")
            }
            .addOnFailureListener{
                Log.e(TAG, "followUser failed, err=${it.message}")
            }
    }

    fun unfollowUser(followingUser: ECUser, userId: String? = FBAuthRepository.getUser()!!.uid){
        db.reference.child(REF_USERS_FOLLOW).child(userId!!).child(followingUser.uid!!).removeValue()
    }


    // Recipes
    fun saveRecipe(recipe: Recipe): DatabaseReference{
        // create or update recipe
        val refRoot = db.reference.child(REF_RECIPES)
        val refRecipe = when{
            recipe.key!= null -> refRoot.child(recipe.key!!)
            else -> refRoot.push()
        }
        Log.d(TAG, "ref.key = ${refRecipe.key}")
        recipe.key = refRecipe.key
        refRecipe.setValue(recipe)
            .addOnCompleteListener{
                if (it.isSuccessful){
                    Log.d(TAG, "saveRecipe success")
                } else {
                    Log.d(TAG, "saveRecipe failed with ${it.exception!!.message}")
                }
            }
        return refRecipe
    }

    fun saveRecipeIngredients(recipeId: String, ingredients: List<Ingredient>){
        db.reference.child(REF_INGREDIENTS).child(recipeId).setValue(ingredients)
    }

    fun saveRecipeSteps(recipeId: String, steps: List<RecipeStep>){
        db.reference.child(REF_STEPS).child(recipeId).setValue(steps)
    }

    fun getRecipe(recipeId: String): MutableLiveData<Recipe>{
        val result = MutableLiveData<Recipe>()
        db.reference.child(REF_RECIPES).child(recipeId).get()
            .addOnSuccessListener {
                val recipe =  it.getValue<Recipe>()!!.apply {
                    key = recipeId
                }
                result.value = recipe
            }
            .addOnFailureListener{
                Log.e(TAG, "getRecipe failed, err=${it.message}")
            }
        return result
    }

    fun getRecipeIngredients(recipeId: String): MutableLiveData<List<Ingredient>>{
        val result = MutableLiveData<List<Ingredient>>()
        db.reference.child(REF_INGREDIENTS).child(recipeId).get()
            .addOnSuccessListener {
                result.value = it.getValue<List<Ingredient>>()
            }
            .addOnFailureListener{
                Log.e(TAG, "getRecipeIngredients failed, err=${it.message}")
            }
        return result
    }

    fun getRecipeSteps(recipeId: String): MutableLiveData<List<RecipeStep>>{
        val result = MutableLiveData<List<RecipeStep>>()
        db.reference.child(REF_STEPS).child(recipeId).get()
            .addOnSuccessListener {
                result.value = it.getValue<List<RecipeStep>>()
            }
            .addOnFailureListener {
                Log.e(TAG, "getRecipeSteps failed, err=${it.message}")
            }
        return result
    }

    fun deleteRecipe(recipeId: String){
        db.reference.child(REF_INGREDIENTS).child(recipeId).removeValue()
        db.reference.child(REF_STEPS).child(recipeId).removeValue()
        db.reference.child(REF_RECIPES).child(recipeId).removeValue()
    }

    fun getRecipeOptions(): FirebaseRecyclerOptions<Recipe> {
        val recipeRef = db.reference.child(REF_RECIPES)

        return FirebaseRecyclerOptions.Builder<Recipe>()
            .setQuery(recipeRef, Recipe::class.java)
            .build()
    }

    fun getUserRecipeOptions(userId: String? = FBAuthRepository.getUser()!!.uid): FirebaseRecyclerOptions<Recipe> {
        val recipeRef = db.reference.child(REF_RECIPES).orderByChild("authorId").equalTo(userId)

        return FirebaseRecyclerOptions.Builder<Recipe>()
            .setQuery(recipeRef, Recipe::class.java)
            .build()
    }

    fun saveRecipeComment(recipeId: String, comment: UserComment){
        val refComment = db.reference.child(REF_RECIPES_COMMENTS).child(recipeId).push()
        comment.key = refComment.key
        refComment.setValue(comment)
            .addOnCompleteListener{
                if (it.isSuccessful){
                    Log.d(TAG, "saveUserComment success")
                } else {
                    Log.d(TAG, "saveUserComment failed with ${it.exception!!.message}")
                }
        }
    }

    fun getRecipeCommentOptions(recipeId: String): FirebaseRecyclerOptions<UserComment> {
        val refComment = db.reference.child(REF_RECIPES_COMMENTS).child(recipeId)

        return FirebaseRecyclerOptions.Builder<UserComment>()
            .setQuery(refComment, UserComment::class.java)
            .build()
    }


    // Newsfeed
    fun savePost(post: Post){
        val refRoot = db.reference.child(REF_POSTS)
        val refPosts = when{
            post.key != null -> refRoot.child(post.key!!)
            else -> refRoot.push()
        }
        Log.d(TAG, "savePost.key=${refPosts.key}")
        refPosts.setValue(post)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d(TAG, "savePost success")
                } else {
                    Log.d(TAG, "savePost failed with ${it.exception!!.message}")
                }
            }

    }

    fun getAllPostOptions(): FirebaseRecyclerOptions<Post>{
        val refPosts = db.reference.child(REF_POSTS)

        return FirebaseRecyclerOptions.Builder<Post>()
                .setQuery(refPosts, Post::class.java)
                .build()
    }

    fun getUserPostOptions(userId: String? = FBAuthRepository.getUser()!!.uid): FirebaseRecyclerOptions<Post>{
        val refPosts = db.reference.child(REF_POSTS).orderByChild("userId").equalTo(userId)

        return FirebaseRecyclerOptions.Builder<Post>()
            .setQuery(refPosts, Post::class.java)
            .build()
    }

    fun savePostComments(postId: String, comment: UserComment){
        val refComment = db.reference.child(REF_POSTS_COMMENTS).child(postId).push()
        comment.key = refComment.key
        refComment.setValue(comment)
            .addOnCompleteListener{
                if (it.isSuccessful){
                    Log.d(TAG, "saveUserComment success")
                } else {
                    Log.d(TAG, "saveUserComment failed with ${it.exception!!.message}")
                }
            }
    }

    fun getPostCommentsOptions(postId: String): FirebaseRecyclerOptions<UserComment>{
        val refComment = db.reference.child(REF_POSTS_COMMENTS).child(postId)

        return FirebaseRecyclerOptions.Builder<UserComment>()
            .setQuery(refComment, UserComment::class.java)
            .build()
    }













}