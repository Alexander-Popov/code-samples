<?php
namespace app\Models\Form\City;

use app\Helper\View\CityViewHelper;
use app\Core\Domain\Repository\City\CityReadRepository;
use yii\base\Model;

class CreateCityForm extends Model
{
    /**
     * @var string
     */
    public $name;

    /**
     * @var string
     */
    public $country;

    /**
     * @var int
     */
    public $extraCharge;

    /**
     * @var CityReadRepository
     */
    private $cityReadRepository;

    /**
     * City constructor.
     * @param CityReadRepository $cityReadRepository
     * @param array $config
     */
    public function __construct(CityReadRepository $cityReadRepository, $config = [])
    {
        $this->cityReadRepository = $cityReadRepository;
        parent::__construct($config);
    }

    /**
     * @inheritdoc
     */
    public function rules()
    {
        return [
            [['name'], 'trim'],
            [['name', 'country'], 'required'],
            ['extraCharge', 'integer', 'max' => 100, 'min' => -100],
            ['name', 'validateName'],
            ['country', 'in', 'range' => array_keys(CityViewHelper::getCountriesArray())],

        ];
    }

    /**
     * @inheritdoc
     */
    public function attributeLabels()
    {
        return array_merge(
            CityViewHelper::attributeLabels()
        );
    }

    /**
     * @param $attribute
     */
    public function validateName($attribute)
    {
        if ($this->cityReadRepository->existsByNameAndCountry($this->name, $this->country)) {
            $this->addError($attribute,  'В этой стране город с таким именем уже существует.');
        }
    }
}
