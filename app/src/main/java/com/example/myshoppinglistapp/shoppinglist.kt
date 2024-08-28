package com.example.myshoppinglistapp

import android.widget.NumberPicker.OnValueChangeListener
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class  shoppingItem(val id:Int,
                         var name:String,
                         var quantity:Int,
                         var isediting:Boolean=false
)



@Composable
fun shopinglistapp(){
    var showdialog by remember { mutableStateOf(false) }
    var itemname by remember {
        mutableStateOf("")
    }
    var itemquantity  by remember {
        mutableStateOf("")
    }
    var sitems by remember{
        mutableStateOf(listOf<shoppingItem>())
    }
    Column(modifier = Modifier
        .fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        Button(onClick = { showdialog=true},
            modifier = Modifier.padding(16.dp)
                .border(
                    border = BorderStroke(2.dp, Color.Black),
                    shape = RoundedCornerShape(20)
                )
        ) {
            Text(text = "Add Item")
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        )
        //Backend/working of edit button after successfully creating the add item UI
        {
            items(sitems){
             item->
                if(item.isediting) {
                    ShoppingitemEditor(item =item, onEditComplete ={
                        editname,editquantity->
                        sitems=sitems.map { it.copy(isediting = false) }
                        val editeditem=sitems.find { it.id==item.id }
                        editeditem?.let{
                            it.name=editname
                            it.quantity=editquantity
                        }
                    })
                }
                else{
                    shoppingListitem(item =item, onEditClick = {
                        sitems=sitems.map { it.copy(isediting = it.id==item.id) }
                    }, onDeleteClick = {
                        sitems = sitems - item
                    }


                    )
                }
            }
        }
    }
    if(showdialog){
     AlertDialog(onDismissRequest = { showdialog=false },
         confirmButton = {
                         Row(modifier = Modifier
                             .fillMaxWidth()
                             .padding(8.dp),
                             horizontalArrangement = Arrangement.SpaceBetween) {
                             Button(onClick = {
                                 if(itemname.isNotBlank()){
                                     val newItem=shoppingItem(
                                         id=sitems.size+1,
                                         name = itemname,
                                         quantity=itemquantity.toInt()
                                     )
                                     sitems=sitems+newItem
                                     showdialog=false
                                     itemname=""
                                 }

                             }) {




                                 Text(text = "Add")

                             }
                             Button(onClick = { showdialog=false }) {
                                 Text(text = "Cancel")

                             }

                         }


         },
        title= {Text(text = "Add Shopping Item")},
         text={
             Column {
                 OutlinedTextField(
                     value = itemname,
                     onValueChange = { itemname = it },
                     singleLine = true,
                     modifier = Modifier
                         .fillMaxWidth()
                         .padding(16.dp)
                 )


                 Column {
                     OutlinedTextField(
                         value = itemquantity,
                         onValueChange = { itemquantity = it },
                         singleLine = true,
                         modifier = Modifier
                             .fillMaxWidth()
                             .padding(16.dp)

                     )

                 }
             }

         }
     )


        }
    }
@Composable
fun shoppingListitem(
    item:shoppingItem,
    onEditClick:()->Unit,
    onDeleteClick:()->Unit
    ) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(
                border = BorderStroke(2.dp, Color.Cyan),
                shape = RoundedCornerShape(20)
            ), horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = item.name, modifier = Modifier.padding(8.dp))
        Text(text = "qty:${item.quantity}", modifier = Modifier.padding(8.dp))
        Row(modifier = Modifier.padding(8.dp)) {
            IconButton(onClick = onEditClick) {
                //Adding edit icon on the list
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            }
            IconButton(onClick = onDeleteClick) {
                //Adding delete icon on the list
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
        }
    }
}
@Composable
fun ShoppingitemEditor(item: shoppingItem,onEditComplete: (String,Int) -> Unit){
    var editname by remember { mutableStateOf(item.name) }
    var editqty by remember{ mutableStateOf(item.quantity.toString()) }
    var isediting by remember { mutableStateOf(item.isediting) }

    Row (modifier= Modifier
        .fillMaxWidth()
        .background(Color.White)
        .padding(8.dp),
        horizontalArrangement =Arrangement.SpaceEvenly)

    {
        Column {
            BasicTextField(
                value = editname, onValueChange = {editname = it},
                singleLine=true,
                modifier= Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            )
            BasicTextField(
                value = editqty, onValueChange = {editqty = it},
                singleLine=true,
                modifier= Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            )
        }
        Button(
            onClick = {
                isediting=false
                onEditComplete(editname,editqty.toIntOrNull()?:1)
            })

        {
            Text("save")

        }
    }


}


