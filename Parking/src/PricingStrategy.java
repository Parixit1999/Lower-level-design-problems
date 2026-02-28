import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.UUID;

interface PricingStrategy{
    double calculateAmount(long hours);
}
