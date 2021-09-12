package ntu.platform.cookery.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import ntu.platform.cookery.BR
import ntu.platform.cookery.databinding.ItemRecipeBinding
import ntu.platform.cookery.data.entity.Recipe
import ntu.platform.cookery.base.BaseClickedListener
import ntu.platform.cookery.base.BaseRecyclerViewHolder


class RecipeAdapter2(
    options: FirebaseRecyclerOptions<Recipe>
): FirebaseRecyclerAdapter<Recipe, RecipeAdapter2.RecipeViewHolder>(options) {
    companion object{
        const val ACTION_ITEM_CLICK = 1001
    }

    var clickedListener: BaseClickedListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecipeBinding.inflate(inflater, parent, false)
        return RecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int, model: Recipe) {
        holder.bindAs(model, clickedListener)
    }


    // ViewHolder
    class RecipeViewHolder(binding: ViewDataBinding) : BaseRecyclerViewHolder(binding){
        fun bindAs(recipe: Recipe, clickedListener: BaseClickedListener?){
            binding.setVariable(BR.item, recipe)
            itemView.setOnClickListener{
                clickedListener?.onClick(ACTION_ITEM_CLICK, this)
            }
        }
    }


    // TODO: without notifyDataSetChanged will lead to IndexOutOfBoundsException, debug later
    override fun startListening() {
        super.startListening()
        notifyDataSetChanged()
    }

    override fun stopListening() {
        super.stopListening()
        notifyDataSetChanged()
    }
}