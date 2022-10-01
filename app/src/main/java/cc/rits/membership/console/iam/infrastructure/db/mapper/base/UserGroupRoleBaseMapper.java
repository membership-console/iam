package cc.rits.membership.console.iam.infrastructure.db.mapper.base;

import cc.rits.membership.console.iam.infrastructure.db.entity.UserGroupRole;
import cc.rits.membership.console.iam.infrastructure.db.entity.UserGroupRoleExample;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

@Mapper
public interface UserGroupRoleBaseMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_group_role
     *
     * @mbg.generated
     */
    long countByExample(UserGroupRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_group_role
     *
     * @mbg.generated
     */
    int deleteByExample(UserGroupRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_group_role
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(@Param("userGroupId") Integer userGroupId, @Param("roleId") Integer roleId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_group_role
     *
     * @mbg.generated
     */
    int insert(UserGroupRole row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_group_role
     *
     * @mbg.generated
     */
    int insertSelective(UserGroupRole row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_group_role
     *
     * @mbg.generated
     */
    List<UserGroupRole> selectByExampleWithRowbounds(UserGroupRoleExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_group_role
     *
     * @mbg.generated
     */
    List<UserGroupRole> selectByExample(UserGroupRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_group_role
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("row") UserGroupRole row, @Param("example") UserGroupRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_group_role
     *
     * @mbg.generated
     */
    int updateByExample(@Param("row") UserGroupRole row, @Param("example") UserGroupRoleExample example);
}