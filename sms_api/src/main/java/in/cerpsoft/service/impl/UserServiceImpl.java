package in.cerpsoft.service.impl;

import in.cerpsoft.repository.UserRepository;
import in.cerpsoft.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
}
