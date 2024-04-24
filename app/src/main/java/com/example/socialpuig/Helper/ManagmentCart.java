package com.example.socialpuig.Helper;

import android.content.Context;
import android.widget.Toast;


import com.example.socialpuig.Domain.ItemsDomain;

import java.util.ArrayList;

public class ManagmentCart {

    private Context context;
    private TinyDB tinyDB;
    private ArrayList<ItemsDomain> cartItems;

    public ManagmentCart(Context context) {
        this.context = context;
        this.tinyDB = new TinyDB(context);
        cartItems = new ArrayList<>();
    }
    public void clearCart() {
        cartItems.clear();
        tinyDB.putListObject("CartList", cartItems); // Actualiza los datos en TinyDB después de limpiar el carrito

    }

    public void insertItem(ItemsDomain item) {
        ArrayList<ItemsDomain> listfood = getListCart();
        boolean existAlready = false;
        int n = 0;
        for (int y = 0; y < listfood.size(); y++) {
            if (listfood.get(y).getTitle().equals(item.getTitle())) {
                existAlready = true;
                n = y;
                break;
            }
        }
        if (existAlready) {
            listfood.get(n).setNumberinCart(item.getNumberinCart());
        } else {
            listfood.add(item);
        }
        tinyDB.putListObject("CartList", listfood);
        Toast.makeText(context, "Added to your Cart", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<ItemsDomain> getListCart() {
        return tinyDB.getListObject("CartList");
    }

    public void minusItem(ArrayList<ItemsDomain> listfood, int position, ChangeNumberItemsListener changeNumberItemsListener) {
        if (listfood.get(position).getNumberinCart() == 1) {
            listfood.remove(position);
        } else {
            listfood.get(position).setNumberinCart(listfood.get(position).getNumberinCart() - 1);
        }
        tinyDB.putListObject("CartList", listfood);
        changeNumberItemsListener.changed();
    }

    public void plusItem(ArrayList<ItemsDomain> listfood, int position, ChangeNumberItemsListener changeNumberItemsListener) {
        listfood.get(position).setNumberinCart(listfood.get(position).getNumberinCart() + 1);
        tinyDB.putListObject("CartList", listfood);
        changeNumberItemsListener.changed();
    }

    public Double getTotalFee() {
        ArrayList<ItemsDomain> listfood2 = getListCart();
        double fee = 0;
        for (int i = 0; i < listfood2.size(); i++) {
            fee = fee + (listfood2.get(i).getPrice() * listfood2.get(i).getNumberinCart());
        }
        return fee;
    }

    public double getTotalWithTaxAndDelivery() {
        // Calcula el precio total de los productos en el carrito
        double subtotal = getTotalFee();

        // Calcula el impuesto
        double tax = Math.round((subtotal * 0.02 * 100.0)) / 100.0;

        // Tarifa de entrega fija
        double deliveryFee = 10.0;

        // Calcula el precio total de la compra incluyendo impuestos y tarifa de entrega
        double total = Math.round((subtotal + tax + deliveryFee) * 100) / 100;

        return total;
    }
}
