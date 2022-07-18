package bo.custom;

import bo.SuperBo;
import dto.OrderDetailsDto;

import java.util.ArrayList;

public interface SystemReportBo extends SuperBo {
    ArrayList<OrderDetailsDto> mostMovableItem() throws Exception,ClassNotFoundException;
    ArrayList<OrderDetailsDto> leastMovableItem() throws Exception,ClassNotFoundException;
}