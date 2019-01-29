package matveyeva.notes

import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.TextView
import kotlinx.android.synthetic.main.main_card_view.view.*


class RecyclerAdapter(val userList:ArrayList<String>): RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private val nListeners: MutableList<NoteListener> = mutableListOf()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtNote.text = userList[position]

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent!!.getContext())
        val view = inflater.inflate(R.layout.main_card_view, parent, false)

        return ViewHolder(view).listen{ position,type->
            selectNote(ViewHolder(view))
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val txtNote = itemView.findViewById<TextView>(R.id.textNote)

    }
    fun addListener(listener: NoteListener){
        nListeners.add(listener)
    }
    fun addItem(name: String) {
        userList.add(name)
        notifyItemInserted(userList.size)
    }

    fun removeAt(position: Int) {
        userList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, userList.size)
    }

    fun selectNote(holder: ViewHolder){
        nListeners.forEach{
            it.noteSelected(holder.itemView.textNote.text.toString())
        }
    }
    fun <T: RecyclerView.ViewHolder> T.listen(event:(position:Int,type:Int)->Unit):T{
        itemView.setOnClickListener{
            event.invoke(adapterPosition,getItemViewType())
        }
        return this
    }

}



