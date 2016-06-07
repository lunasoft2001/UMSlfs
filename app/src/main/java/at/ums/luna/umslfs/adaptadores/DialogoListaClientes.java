package at.ums.luna.umslfs.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import at.ums.luna.umslfs.R;
import at.ums.luna.umslfs.modelos.Clientes;

/**
 * Created by luna-aleixos on 07.06.2016.
 */
public class DialogoListaClientes extends BaseAdapter{

    private ArrayList<Clientes> listData;

    private LayoutInflater layoutInflater;

    public DialogoListaClientes(Context context, ArrayList<Clientes> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.dialogo_lista_clientes_items, null);
            holder = new ViewHolder();
            holder.idCliente = (TextView) convertView.findViewById(R.id.idCliente);
            holder.nombreCliente = (TextView) convertView.findViewById(R.id.nombreCliente);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.idCliente.setText(String.valueOf(listData.get(position).getId()));
        holder.nombreCliente.setText(listData.get(position).getNombre().toString());

        return convertView;
    }

    static class ViewHolder {
        TextView idCliente;
        TextView nombreCliente;
    }

}