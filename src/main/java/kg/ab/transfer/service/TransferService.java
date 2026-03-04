package kg.ab.transfer.service;

import kg.ab.transfer.model.payload.request.TransferRequest;
import kg.ab.transfer.model.payload.response.TransferResponse;

public interface TransferService {

    TransferResponse transfer(TransferRequest request);
}
