package org.flowable.ui.modeler.rest.app;

import org.flowable.ui.common.model.ResultListDataRepresentation;
import org.flowable.ui.common.model.UserRepresentation;
import org.flowable.ui.common.service.idm.RemoteIdmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/app")
public class EditorUsersResource {

    @Autowired
    protected RemoteIdmService remoteIdmService;

    @RequestMapping(value = "/rest/editor-users", method = RequestMethod.GET)
    public ResultListDataRepresentation getUsers(@RequestParam(value = "filter", required = false) String filter) {
        //List<? extends User> matchingUsers = remoteIdmService.findUsersByNameFilter(filter);
        //List<UserRepresentation> userRepresentations = new ArrayList<>(matchingUsers.size());
        List<UserRepresentation> userRepresentations = new ArrayList<>();
        /*for (User user : matchingUsers) {
            userRepresentations.add(new UserRepresentation(user));
        }*/
        UserRepresentation u1 = new UserRepresentation();
        u1.setEmail("395471046@qq.com");
        u1.setFirstName("姚利国");
        u1.setFullName("姚利国2");
        u1.setId("123");

        UserRepresentation u2 = new UserRepresentation();
        u2.setEmail("395471046@qq.com");
        u2.setFirstName("张三");
        u2.setFullName("张三2");
        u2.setId("456");

        userRepresentations.add(u1);
        userRepresentations.add(u2);
        //u1.set
        return new ResultListDataRepresentation(userRepresentations);
    }

}
