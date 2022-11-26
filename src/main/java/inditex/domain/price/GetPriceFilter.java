package inditex.domain.price;

public record GetPriceFilter(
   long productId,
   int brandId,
   long date
) {}
