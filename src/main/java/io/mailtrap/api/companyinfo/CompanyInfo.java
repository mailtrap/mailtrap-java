package io.mailtrap.api.companyinfo;

import io.mailtrap.model.request.companyinfo.CreateCompanyInfoRequest;
import io.mailtrap.model.request.companyinfo.UpdateCompanyInfoRequest;
import io.mailtrap.model.response.companyinfo.CompanyInfoResponse;

public interface CompanyInfo {

    /**
     * Get the company info associated with a domain
     *
     * @param domainId unique domain ID
     * @return company info of the domain
     */
    CompanyInfoResponse getCompanyInfo(long domainId);

    /**
     * Create the company info for a domain. Company info is required for
     * domain compliance verification.
     *
     * @param domainId unique domain ID
     * @param request  request data
     * @return created company info
     */
    CompanyInfoResponse createCompanyInfo(long domainId, CreateCompanyInfoRequest request);

    /**
     * Update the company info for a domain
     *
     * @param domainId unique domain ID
     * @param request  request data, only the fields set are sent
     * @return updated company info
     */
    CompanyInfoResponse updateCompanyInfo(long domainId, UpdateCompanyInfoRequest request);
}
