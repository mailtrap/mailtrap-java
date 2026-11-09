package io.mailtrap.api.companyinfo;

import io.mailtrap.Constants;
import io.mailtrap.api.apiresource.ApiResource;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.http.RequestData;
import io.mailtrap.model.request.companyinfo.CreateCompanyInfoRequest;
import io.mailtrap.model.request.companyinfo.UpdateCompanyInfoRequest;
import io.mailtrap.model.response.companyinfo.CompanyInfoResponse;

public class CompanyInfoImpl extends ApiResource implements CompanyInfo {

    public CompanyInfoImpl(final MailtrapConfig config) {
        super(config);
        this.apiHost = Constants.GENERAL_HOST;
    }

    @Override
    public CompanyInfoResponse getCompanyInfo(final long domainId) {
        return httpClient.get(
            companyInfoPath(domainId),
            new RequestData(),
            CompanyInfoResponse.class
        );
    }

    @Override
    public CompanyInfoResponse createCompanyInfo(final long domainId, final CreateCompanyInfoRequest request) {
        return httpClient.post(
            companyInfoPath(domainId),
            request,
            new RequestData(),
            CompanyInfoResponse.class
        );
    }

    @Override
    public CompanyInfoResponse updateCompanyInfo(final long domainId, final UpdateCompanyInfoRequest request) {
        return httpClient.patch(
            companyInfoPath(domainId),
            request,
            new RequestData(),
            CompanyInfoResponse.class
        );
    }

    private String companyInfoPath(final long domainId) {
        return String.format(apiHost + "/api/domains/%d/company_info", domainId);
    }
}
