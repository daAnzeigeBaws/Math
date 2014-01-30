class Addition implements FormulaPart
{
  private FormulaPart fp1;
  private FormulaPart fp2;
  
  public Addition(FormulaPart f1, FormulaPart f2)
  {
    fp1 = f1;
    fp2 = f2;
  }
  
  public Numb result()
  {
    return Numb.add(fp1.result(),fp2.result());
  }
  
  public String toString()
  {
    return fp1.toString()+" "+"+"+" "+fp2.toString();
  }
}