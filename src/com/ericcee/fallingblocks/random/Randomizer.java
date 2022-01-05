package com.ericcee.fallingblocks.random;

import java.util.ArrayList;
import java.util.Random;

public class Randomizer extends Random {
	
	private class numItem{
		private int number=0;
		private int itter =0;
		public numItem(int number){
			this.number=number;
		}
		
		public int getNumber(){
			return number;
		}
		
		public int getItter(){
			return itter;
		}
		
		public void hasTaken(){
			itter++;
		}
	}
	
	ArrayList <numItem> nList = new ArrayList<>();
	numItem last = null;
	
	int min=0,max=0;
	
	public Randomizer(int min,int max){
		super();
		this.min=min;
		this.max=max;
		for(int i = min;i<max+1;i++){
			nList.add(new numItem(i));
		}
	}
	
	
	private numItem getMaxItter(ArrayList<numItem> items){
		numItem err = new numItem(0);
		for(numItem i : items){
			if(i.getItter()>err.getItter()) err=i;
		}
		return err;
	}
	
	private ArrayList<numItem> getItemWithTolerance(ArrayList <numItem> itm, int tol){
		ArrayList<numItem> items = new ArrayList<>();
		numItem mx = getMaxItter(itm);
		
		for(numItem i : itm){
			if(mx.getItter()-i.getItter()>=tol) items.add(i);
		}
		
		if(items.toArray().length==0)return itm;
		return getItemWithTolerance(items, tol);
	}
	
	
	public int getNextNum(){
		ArrayList<numItem> itms = getItemWithTolerance(nList, 1);
		numItem choosen = null;
		boolean found = false;
		while(!found){
			if(itms.toArray().length==0){
				choosen=(numItem)nList.toArray()[this.nextInt(nList.toArray().length)];
				
			}
			else{
				choosen=(numItem)itms.toArray()[this.nextInt(itms.toArray().length)];
			}
			
			if(last==null) {
				found=true;
				last=new numItem(choosen.getNumber());
			}
			else if(choosen.getNumber()!=last.getNumber()) {
				found=true;
				last=new numItem(choosen.getNumber());
			}
			else if(last.getItter()<=1){
				found=true;
			}
			
		}
		choosen.hasTaken();
		return choosen.getNumber();
	}
	
	
	public String toString(){
		String ret = "";
		for(numItem x: nList){
			ret+=(x.getNumber()+" "+x.getItter())+"\n";
		}
		return ret;
	}
}
