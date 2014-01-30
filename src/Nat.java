class Nat extends Numb
{
  protected Numb pre;
  
  protected Nat()
  {
    pre = new O();
  }
  protected Nat(Numb n)
  {
    pre = n;
  }
  
  public Numb result()
  {
    return this;
  }
  
  public String toString()
  {
    return pre.toString()+"'";
  }
}