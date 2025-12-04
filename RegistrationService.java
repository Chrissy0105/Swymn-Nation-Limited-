package com.swymnation.dms.service;

import com.swymnation.dms.model.Client;
import com.swymnation.dms.controller.dto.RegistrationForm;
import com.swymnation.dms.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrationService {

    private final ClientRepository clientRepository;
    private final PaymentGatewayProxy paymentGatewayProxy;

    @Autowired
    public RegistrationService(ClientRepository clientRepository, PaymentGatewayProxy paymentGatewayProxy) {
        this.clientRepository = clientRepository;
        this.paymentGatewayProxy = paymentGatewayProxy;
    }

    @Transactional
    public Client registerClient(RegistrationForm form) {
        Client client = new Client(
            form.getFirstName(), form.getLastName(), form.getEmail(), 
            form.getPhoneNumber(), form.getAddress()
        );

        Client savedClient = clientRepository.save(client);
        paymentGatewayProxy.initiatePayment(savedClient);
        
        return savedClient;
    }
}