class FatalMathException extends Exception
{
  public String toString()
  {
    return "Fatal Math Exception: " + getCause();
  }
}