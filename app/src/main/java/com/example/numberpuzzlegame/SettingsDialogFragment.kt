package com.example.numberpuzzlegame

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.annotation.Size
import  androidx.fragment.app.DialogFragment
class SettingsDialogFragment (var size: Int) : DialogFragment(){

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {


        val builder = AlertDialog.Builder(activity)
            .setTitle("Define the size of the puzzle")
            .setSingleChoiceItems(R.array.size_options,size-2){
                dialog,which->
                size = which + 2
            }
            .setPositiveButton("change "){
                dialog,id->
                (getActivity() as MainActivity).changeSize(size)

            }
            .setNegativeButton("cancel"){
                dialog, _-> dialog.cancel()
            }
        val settingsDialog = builder.create()
        settingsDialog.show()
        return settingsDialog
    }
}







//This SettingsDialogFragment allows users to select the size of the puzzle in the number puzzle game through a dialog
// interface, enhancing the user experience and customization options.