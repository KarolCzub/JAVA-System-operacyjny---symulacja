package OS;

import java.util.List;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.Iterator;

public class PageTable {
	
	public int pageSize = 16;	
	public Pamieci [] str;
		
	public PageTable()
	{

	}
	
	public PageTable(int rozmiar)
	{
		int a = rozmiar/16;
		if(rozmiar%16!=0) a++;
		str = new Pamieci[a];
		
		for(int i=0;i<a;i++)
		{
			str[i] = new Pamieci();
			str[i].nr_w_pam_op = -1;
			str[i].nr_w_pam_wt = -1;
		}
	}

	
	private int ile(int rozmiar)
	{
		int ile_ramek;
		if(rozmiar%pageSize!=0) 
		{
			ile_ramek = rozmiar / pageSize + 1;
		}
		else ile_ramek = rozmiar / pageSize;
		return ile_ramek;
	}
	
	
	public int ramka(int add)
	{
		int nr_r = ile(add);
		return nr_r;
	}
	
	public int offset(int add)
	{
		int off = add%pageSize;
		return off;
	}
	
	public void jak_duza(int rozmiar)
	{
		int a = rozmiar/16;
		if(rozmiar%16!=0) a++;
		str = new Pamieci[a];
		
		for(int i=0;i<a;i++)
		{
			str[i] = new Pamieci();
			str[i].nr_w_pam_op = -1;
			str[i].nr_w_pam_wt = -1;
		}
	}
	
	
}
